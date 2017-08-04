package com.truck.monitor.weixin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.truck.monitor.domain.Driver;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.truck.monitor.App;
import com.truck.monitor.domain.Follower;
import com.truck.monitor.domain.User;
import com.truck.monitor.domain.User.Type;
import com.truck.monitor.service.DriverService;
import com.truck.monitor.service.FollowerService;
import com.truck.monitor.service.UserService;
import com.truck.monitor.weixin.config.Config;

@Controller
@RequestMapping("wx")
public class WeiXinController {

	protected Logger	        logger	= LoggerFactory.getLogger(getClass());

	@Autowired
	protected WxMpConfigStorage	storage;

	@Autowired
	protected WxMpService	    wxMpService;

	@Autowired
	private MyWeiXinService	    myWeiXinService;

	@Autowired
	private Config	            config;

	@Autowired
	private UserService	        userService;

	@Autowired
	private DriverService	    driverService;

	@Autowired
	private FollowerService	    followerService;

	@RequestMapping(value = "/index")
	public String index(String code, String state) throws WxErrorException {
		String openid = wxMpService.oauth2getAccessToken(code).getOpenId();
		Follower follower = followerService.findOne(openid);
		if (null == follower) {
			WxMpUser wxMpUser = myWeiXinService.getUserInfo(openid, "zh_CN");
			follower = new Follower();
			follower.setHeadImgUrl(wxMpUser.getHeadImgUrl());
			follower.setNickname(App.trimOffUTF8(wxMpUser.getNickname()));
			follower.setOpenid(openid);
			followerService.save(follower);
		}
		User self = userService.findByFollower(new Follower(openid));
		if (self == null) {
			return "redirect:" + config.getHost() + "wechat/register.html?openid=" + openid;
		} else {
			if (new Integer(1).equals(self.getStatus())) {
				if (self.getType().equals(Type.DRIVER)) {
					// 如果是车队长
					Driver driver = driverService.findOne(self.getId());
					if (null != driver && driver.getCaptain() == Boolean.TRUE) { // 如果是车队长
						return "redirect:" + config.getHost() + "wechat/captain_index.html?openid=" + openid;
					}
					return "redirect:" + config.getHost() + "wechat/index.html?openid=" + openid;
				} else if (self.getType().equals(Type.DIRECTOR)) {
					return "redirect:" + config.getHost() + "wechat/director_index.html?openid=" + openid;
				} else {
					return "redirect:" + config.getHost() + "wechat/admin.html?openid=" + openid;
				}
			} else {
				return "redirect:" + config.getHost() + "wechat/locked.html?openid=" + openid;
			}
		}
	}

	/**
	 * 公众号.
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/mp", method = {RequestMethod.GET, RequestMethod.POST})
	public void mp(HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		String signature = request.getParameter("signature");
		String nonce = request.getParameter("nonce");
		String timestamp = request.getParameter("timestamp");

		if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
			// 消息签名不正确，说明不是公众平台发过来的消息
			response.getWriter().println("非法请求");
			return;
		}
		String echoStr = request.getParameter("echostr");
		if (StringUtils.isNotBlank(echoStr)) {
			// 说明是一个仅仅用来验证的请求，回显echostr
			String echoStrOut = String.copyValueOf(echoStr.toCharArray());
			response.getWriter().println(echoStrOut);
			return;
		}

		String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type")) ? "raw" : request
		        .getParameter("encrypt_type");

		if ("raw".equals(encryptType)) {
			// 明文传输的消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
			// System.out.println("@@@@@@ : " + JSON.toJSONString(inMessage));
			WxMpXmlOutMessage outMessage = myWeiXinService.route(inMessage);
			if (outMessage != null) {
				String msg = outMessage.toXml();
				System.out.println("明文验证结果 : " + msg);
				response.getWriter().write(msg);
			}
			return;
		}

		if ("aes".equals(encryptType)) {
			// 是aes加密的消息
			String msgSignature = request.getParameter("msg_signature");
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), storage, timestamp,
			        nonce, msgSignature);
			this.logger.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
			WxMpXmlOutMessage outMessage = myWeiXinService.route(inMessage);
			this.logger.info(response.toString());
			String msg = outMessage.toEncryptedXml(storage);
			System.out.println("加密验证结果 ： " + msg);
			response.getWriter().write(msg);
			return;
		}
		response.getWriter().println("不可识别的加密类型");
		System.out.println("不可识别的加密类型");
		return;
	}

	@RequestMapping(value = "/jssdk", method = RequestMethod.POST)
	@ResponseBody
	public WxJsapiSignature jssdk(final String url, final String link) {
		if (logger.isInfoEnabled()) {
			logger.info("本次验证的[url={}, link={}]", url, link);
		}
		try {
			return wxMpService.createJsapiSignature(url);
		} catch (WxErrorException e) {
			logger.error("获取JSAPI校验失败", e);
		}
		return null;
	}
}
