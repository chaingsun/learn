package com.truck.monitor.service;

import java.math.BigDecimal;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truck.monitor.domain.Behavior;
import com.truck.monitor.domain.ConstructionSiteQrCode;
import com.truck.monitor.domain.Scan;
import com.truck.monitor.repository.HDRepository;
import com.truck.monitor.repository.ScanRepository;

@Service
@Transactional
public class ScanServiceImpl extends HDServiceImpl<Scan> implements ScanService {

	@Autowired
	private ScanRepository repository;

	@Autowired
	private ConstructionSiteQrCodeService qrCodeService;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 计算两个经纬度间的距离.
	 * 
	 * @param long1
	 * @param lat1
	 * @param long2
	 * @param lat2
	 * @return
	 */
	public static double getDistance(double long1, double lat1, double long2, double lat2) {
		final double EARTH_RADIUS = 6378.137;
		double a, b, d, sa2, sb2;
		lat1 = rad(lat1);
		lat2 = rad(lat2);
		a = lat1 - lat2;
		b = rad(long1 - long2);

		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
		return d;
	}

	public Scan add(Scan scan) {
		/**
		 * 所扫描的二维码.
		 */
		ConstructionSiteQrCode qrCode = qrCodeService.findOne(scan.getQrCode().getId());
		scan.setTime(new Date());
		Behavior behavior = scan.getBehavior();
		scan.setDistance(new BigDecimal(getDistance(Double.parseDouble(behavior.getLongitude()), Double.parseDouble(behavior.getLatitude()),
				Double.parseDouble(qrCode.getLongitude()), Double.parseDouble(qrCode.getLatitude()))).longValue());
		return super.add(scan);
	}

	public HDRepository<Scan> getRepository() {
		return repository;
	}

	public Scan findByBehavior(Behavior behavior) {
		return repository.findByBehavior(behavior);
	}
}
