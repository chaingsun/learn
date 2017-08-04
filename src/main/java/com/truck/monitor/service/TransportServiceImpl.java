package com.truck.monitor.service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.criteria.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.truck.monitor.domain.*;
import com.truck.monitor.domain.Behavior.Type;
import com.truck.monitor.repository.DriverRepository;
import com.truck.monitor.repository.HDRepository;
import com.truck.monitor.repository.TransportRepository;
import com.truck.monitor.repository.UserRepository;
import com.truck.monitor.vo.TransportStatis;
import com.truck.monitor.vo.TransportStatisDetail;

@Service
public class TransportServiceImpl extends HDServiceImpl<Transport> implements TransportService {

	@Autowired
	private TransportRepository	  repository;

	@Autowired
	private DriverRepository	  driverRepository;

	@Autowired
	private UserRepository	      userRepository;

	@Autowired
	private MaterielAmountService	materielAmountService;

	public HDRepository<Transport> getRepository() {
		return repository;
	}

	@Override
	public Transport add(Transport transport) {
		Calendar cal = Calendar.getInstance();
		transport.setCode(String.valueOf(System.currentTimeMillis()));
		transport.setTime(cal.getTime());
		transport.setYear(cal.get(Calendar.YEAR));
		transport.setMonth(cal.get(Calendar.MONTH) + 1);
		transport.setDay(cal.get(Calendar.DAY_OF_MONTH));
		return super.add(transport);
	}

	private static Date getBeginTime(Date time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	private static Date getEndTime(Date time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	public List<Map<String, Object>> findStatisticsByYearsAndMonths(Driver driver, Integer[] years, Integer[] months) {
		List<Map<String, Object>> statistics = null;
		if (driver == null || driver.getId() == null) {
			statistics = repository.findStatisticsByYearsAndMonths(years, months);
		} else {
			statistics = repository.findStatisticsByYearsAndMonths4Driver(driver, years, months);
		}
		for (Map<String, Object> map : statistics) {
			Integer year = (Integer) map.get("year");
			Integer month = (Integer) map.get("month");
			Materiel materiel = (Materiel) map.get("materiel");
			ConstructionSite site = (ConstructionSite) map.get("site");
			MaterielAmount ma = materielAmountService.findByMaterielAndSiteAndYearAndMonth(materiel, site, year, month);
			if (ma != null) {
				map.put("totalAmount", ma.getAmount());
			}

		}
		return statistics;
	}

	@Override
	protected Predicate toPredicate(Transport transport, Root<Transport> root, CriteriaQuery<?> query,
	        CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();
		Join<Transport, Driver> driver = root.join(root.getModel().getSingularAttribute("driver", Driver.class),
		        JoinType.INNER);
		Join<Transport, ConstructionSite> site = root.join(
		        root.getModel().getSingularAttribute("constructionSite", ConstructionSite.class), JoinType.INNER);
		/**
		 * 司机.
		 */
		if (transport.getDriver() != null) {
			if (!StringUtils.isEmpty(transport.getDriver().getName())) {
				predicates.add(cb.like(driver.get("name").as(String.class), "%" +
				        transport.getDriver().getName().trim() + "%"));
			}
			if (transport.getDriver().getId() != null) {
				predicates.add(cb.equal(driver.get("id").as(Long.class), transport.getDriver().getId()));
			}
		}
		if (transport.getTime() != null) {
			Date beginTime = getBeginTime(transport.getTime());
			Date endTime = getEndTime(transport.getTime());
			predicates.add(cb.between(root.<Date> get("time"), beginTime, endTime));
		}
		if (transport.getConstructionSite() != null && transport.getConstructionSite().getId() != null) {
			predicates.add(cb.equal(site.get("id").as(Long.class), transport.getConstructionSite().getId()));
		}
		return cb.and(predicates.toArray(new Predicate[predicates.size()]));
	}

	@Override
	public TransportStatis statis(Long userId, Date startDate, Date endDate) {
		TransportStatis statis = new TransportStatis();
		//
		User user = userRepository.findOne(userId);
		//
		if (null == user) {
			throw new RuntimeException("系统不存在ID= " + userId + "的用户");
		}
		//
		statis.setEnd(endDate);
		statis.setStart(startDate);
		if (User.Type.DRIVER == user.getType()) {
			Driver driver = driverRepository.findOne(userId);
			statis.setLicensePlate(driver.getLicensePlate());
		}
		//
		List<Transport> data = repository.findAll(new Specification<Transport>() {
			@Override
			public Predicate toPredicate(Root<Transport> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				//
				User.Type type = user.getType();
				if (type == User.Type.DRIVER) {
					predicates.add(cb.equal(root.get("driver").get("id"), userId));
				} else if (type == User.Type.DIRECTOR) {
					predicates.add(cb.equal(root.get("auditor").get("id"), userId));
				} else {
					// do onthing
				}
				//
				Date beginTime = getBeginTime(startDate);
				Date endTime = getEndTime(endDate);
				//
				predicates.add(cb.between(root.get("time"), beginTime, endTime));
				//
				return cb.and(predicates.toArray(new Predicate[0]));
			}
		});
		//
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		//
		if (null != data) {
			List<TransportStatisDetail> details = data
			        .stream()
			        .collect(Collectors.groupingBy(t -> {
				        Date time = t.getTime();
				        return format.format(time);
			        }))
			        .entrySet()
			        .stream()
			        .map(e -> {
				        TransportStatisDetail detail = new TransportStatisDetail();
				        detail.setDate(e.getKey());
				        List<Transport> transports = e.getValue();
				        detail.setCount(transports.size());
				        Long reward = transports.stream().map(tp -> tp.getReward()).filter(r -> null != r)
				                .reduce(0L, (a, b) -> a + b);
				        detail.setAmount(reward);
				        return detail;
			        }).collect(Collectors.toList());
			//
			statis.setDetails(details);
			Integer integer = details.stream().map(d -> d.getCount()).reduce(0, (a, b) -> a + b);
			if (null != integer) {
				statis.setCount(integer);
			}
		} else {
			statis.setDetails(Collections.emptyList());
		}
		Long amount = statis.getDetails().stream().map(d -> d.getAmount()).reduce(0L, (a, b) -> a + b);
		statis.setAmount(amount);
		//
		return statis;
	}

	public Transport findByDriverAndStatusNot(Driver driver, Type status) {
		return repository.findByDriverAndStatusNot(driver, status);
	}

}
