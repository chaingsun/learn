package com.truck.monitor.weixin.handler;

import java.util.Map;

import com.truck.monitor.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.truck.monitor.domain.Follower;
import com.truck.monitor.service.FollowerService;
import com.truck.monitor.weixin.MyWeiXinService;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutTextMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * 用户关注公众号Handler.
 * 
 * @author lzq
 *
 */
@Component
public class SubscribeHandler implements WxMpMessageHandler {

	@Autowired
	protected MyWeiXinService myWeiXinService;

	@Autowired
	private FollowerService followerService;

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
		String openid = wxMessage.getFromUser();
		WxMpUser wxMpUser = myWeiXinService.getUserInfo(openid, "zh_CN");
		Follower follower = new Follower();
		follower.setHeadImgUrl(wxMpUser.getHeadImgUrl());
		follower.setNickname(App.trimOffUTF8(wxMpUser.getNickname()));
		follower.setOpenid(openid);
		followerService.save(follower);
		WxMpXmlOutTextMessage message = WxMpXmlOutMessage.TEXT().content("尊敬的" + wxMpUser.getNickname() + "，您好！").fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
				.build();
		return message;
	}
};
