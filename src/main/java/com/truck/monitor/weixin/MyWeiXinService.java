package com.truck.monitor.weixin;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truck.monitor.weixin.config.Config;
import com.truck.monitor.weixin.handler.MessageHandler;
import com.truck.monitor.weixin.handler.ScanHandler;
import com.truck.monitor.weixin.handler.SubscribeHandler;
import com.truck.monitor.weixin.handler.UnsubscribeHandler;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMenuService;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

@Service
public class MyWeiXinService {

	@Autowired
	private WxMpService wxMpService;

	@Autowired
	private SubscribeHandler subscribeHandler;

	@Autowired
	private MessageHandler messageHandler;
	@Autowired
	private UnsubscribeHandler unsubscribeHandler;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private WxMpMessageRouter router;

	@Autowired
	private Config config;

	@Autowired
	private ScanHandler scanHandler;

	@PostConstruct
	public void init() {
		//refreshRouter();
		//initMenu();
	}

	protected void initMenu() {
		try {
			WxMpMenuService menuService = wxMpService.getMenuService();
			menuService.menuDelete();

			WxMenu menu = new WxMenu();
			WxMenuButton indexMenu = new WxMenuButton();
			indexMenu.setName("土石方");
			indexMenu.setUrl(wxMpService.oauth2buildAuthorizationUrl(config.getHost() + "wx/index", "snsapi_base", "INDEX"));
			indexMenu.setType(WxConsts.BUTTON_VIEW);
			menu.getButtons().add(indexMenu);

			WxMenuButton scanMenu = new WxMenuButton();
			scanMenu.setName("扫码");
			scanMenu.setType(WxConsts.BUTTON_SCANCODE_PUSH);
			scanMenu.setKey("SCAN");
			menu.getButtons().add(scanMenu);

			menuService.menuCreate(menu);
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
	}

	public void refreshRouter() {

		WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);

		router.rule().msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.BUTTON_SCANCODE_PUSH).handler(scanHandler).end();

		// 关注事件
		router.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_SUBSCRIBE).handler(subscribeHandler).end();

		// 取消关注事件.
		router.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_UNSUBSCRIBE).handler(unsubscribeHandler).end();

		// 默认
		router.rule().async(false).handler(messageHandler).end();

		this.router = router;
	}

	public WxMpXmlOutMessage route(WxMpXmlMessage inMessage) {
		try {
			return this.router.route(inMessage);
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
		}

		return null;
	}

	public WxMpUser getUserInfo(String openid, String lang) {
		try {
			return this.wxMpService.getUserService().userInfo(openid, lang);
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
		return null;
	}

}
