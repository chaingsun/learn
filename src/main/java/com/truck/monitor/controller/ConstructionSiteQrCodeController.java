package com.truck.monitor.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.truck.monitor.domain.Behavior;
import com.truck.monitor.domain.ConstructionSiteQrCode;
import com.truck.monitor.domain.Driver;
import com.truck.monitor.domain.Follower;
import com.truck.monitor.domain.Transport;
import com.truck.monitor.domain.User;
import com.truck.monitor.domain.User.Type;
import com.truck.monitor.service.ConstructionSiteQrCodeService;
import com.truck.monitor.service.HDService;
import com.truck.monitor.service.QRCodeService;
import com.truck.monitor.service.TransportService;
import com.truck.monitor.service.UserService;
import com.truck.monitor.weixin.config.Config;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.api.WxMpService;

@Controller
@RequestMapping("qrcodes")
public class ConstructionSiteQrCodeController extends HDController<ConstructionSiteQrCode> {

	@Autowired
	private ConstructionSiteQrCodeService service;

	@Autowired
	private QRCodeService qrCodeService;

	@Autowired
	private WxMpService wxMpService;

	@Autowired
	private UserService userService;

	@Autowired
	private TransportService transportService;

	@Autowired
	private Config config;

	public HDService<ConstructionSiteQrCode> getService() {
		return service;
	}

	/**
	 * 激活二维码.
	 * 
	 * @param id
	 * @param qrCode
	 * @return
	 */
	@RequestMapping(value = "/{id}/active")
	@ResponseBody
	public Result active(@PathVariable Long id, @RequestBody ConstructionSiteQrCode qrCode) {
		if (StringUtils.isEmpty(qrCode.getLatitude()) || StringUtils.isEmpty(qrCode.getLongitude())) {
			throw new RuntimeException("获取当前位置经纬度失败.");
		}
		ConstructionSiteQrCode entity = service.findOne(id);
		if (entity.getActivated() == null || !entity.getActivated()) {
			entity.setActivated(Boolean.TRUE);
			entity.setPasteAddress(qrCode.getPasteAddress());
			entity.setLatitude(qrCode.getLatitude());
			entity.setLongitude(qrCode.getLongitude());
		}
		service.modify(entity);
		return SUCCESS;
	}

	/**
	 * 扫码.
	 * 
	 * @return
	 * @throws WxErrorException
	 */
	@RequestMapping(value = "/{id}/scan")
	public String scan(@PathVariable Long id, String code, String state) throws WxErrorException {
		String openid = wxMpService.oauth2getAccessToken(code).getOpenId();
		ConstructionSiteQrCode constructionSiteQrCode = service.findOne(id);
		User self = userService.findByFollower(new Follower(openid));
		if (self == null) {
			/**
			 * 未找到openid对应的用户数据.
			 */
			return "redirect:" + config.getHost() + "wechat/register.html?openid=" + openid;
		} else {

			if (new Integer(1).equals(self.getStatus())) {

				/**
				 * 用户为超级管理员时激活二维码.
				 */
				if (self.getType().equals(Type.OPS)) {
					return "redirect:" + config.getHost() + "wechat/active.html?openid=" + openid + "&qrid=" + constructionSiteQrCode.getId();
				}
				/**
				 * 用户为司机时.
				 */
				else if (self.getType().equals(Type.DRIVER)) {

					/**
					 * 查询是否有未完成的运输单.
					 */
					Transport transport = transportService.findByDriverAndStatusNot(new Driver(self.getId()), Behavior.Type.DISCHARGE);

					/**
					 * 无未完成的运输单.
					 */
					if (transport == null) {
						return "redirect:" + config.getHost() + "wechat/smcg.html?openid=" + openid + "&sid=" + constructionSiteQrCode.getConstructionSite().getId() + "&type="
								+ Behavior.Type.IN + "&qrid=" + constructionSiteQrCode.getId();
					} else {
						return "redirect:" + config.getHost() + "wechat/smcg.html?tid=" + transport.getId() + "&openid=" + openid + "&sid="
								+ constructionSiteQrCode.getConstructionSite().getId() + "&type=" + Behavior.Type.OUT + "&qrid=" + constructionSiteQrCode.getId();

					}
				}
				/**
				 * 主管扫码.
				 */
				else if (self.getType().equals(Type.DIRECTOR)) {
					// 未做处理.
				} else {
				}
			} else {
				return "redirect:" + config.getHost() + "wechat/locked.html?openid=" + openid;
			}

		}
		return null;
	}

	@RequestMapping(value = "/{id}/view", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> view(@PathVariable final Long id, HttpServletRequest request) throws IOException {
		ConstructionSiteQrCode qrCode = this.findOne(id);
		InputStream is = null;
		try {
			File file = new File(qrCodeService.getDir() + qrCode.getPath());
			is = new FileInputStream(file);
			byte[] body = new byte[is.available()];
			is.read(body);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attchement;filename=" + file.getName());
			HttpStatus statusCode = HttpStatus.OK;
			return new ResponseEntity<byte[]>(body, headers, statusCode);
		} finally {
			if (is != null) {
				is.close();
			}
		}

	}

}
