package com.truck.monitor.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truck.monitor.domain.*;
import com.truck.monitor.repository.BehaviorRepository;
import com.truck.monitor.repository.HDRepository;

@Service
@Transactional
public class BehaviorServiceImpl extends HDServiceImpl<Behavior> implements BehaviorService {

	@Autowired
	private BehaviorRepository repository;
	@Autowired
	private TransportService transportService;

	@Autowired
	private ScanService scanService;

	@Autowired
	private AssignService assignService;

	@Autowired
	private DriverService driverService;

	@Autowired
	private ConstructionSiteQrCodeService qrCodeService;

	public HDRepository<Behavior> getRepository() {
		return repository;
	}

	public List<Behavior> findByTransportOrderByTimeDesc(Transport transport) {
		return repository.findByTransportOrderByTimeDesc(transport);
	}

	@Override
	public Behavior add(Behavior behavior) {

		Transport transport = behavior.getTransport();
		behavior.setTime(new Date());
		if (behavior.getType().equals(Behavior.Type.IN)) {
			Transport unCompletedTran = transportService.findByDriverAndStatusNot(transport.getDriver(), Behavior.Type.DISCHARGE);
			if (unCompletedTran != null) {
				throw new RuntimeException("运输号：" + unCompletedTran.getCode() + "未完成，不能再次进场");
			}

			Driver driver = driverService.findOne(transport.getDriver().getId());
			/**
			 * 该车队的司机是否需要指派到指定工地.
			 */
			if (driver.getMotorcade().getAssign() != null && driver.getMotorcade().getAssign()) {
				Assign assign = assignService.findByDriverAndDay(driver, DateFormatUtils.format(Calendar.getInstance(), Assign.PATTERN));
				if (assign == null || !assign.getSite().getId().equals(qrCodeService.findOne(behavior.getScan().getQrCode().getId()).getConstructionSite().getId())) {
					throw new RuntimeException("你未被指定到该工地作业.");
				}
			}

			transport.setStatus(Behavior.Type.IN);
			transport.setMotorcade(driver.getMotorcade());
			transportService.add(transport);

		} else {
			transport = transportService.findOne(transport.getId());
			Behavior.Type btype = behavior.getType();

			Behavior.Type status = transport.getStatus();
			if (btype == Behavior.Type.OUT) { // 如果是退场
				if (status == Behavior.Type.OUT) {
					throw new RuntimeException("不允许重复退场");
				}
				if (status == Behavior.Type.DISCHARGE) {
					throw new RuntimeException("该运单已卸货");
				}
				if (behavior.getType().equals(Behavior.Type.OUT) && transport.getAuditTime() == null) {
					throw new RuntimeException("主管未审核，不能退场");
				}
			} else {
				if (status == Behavior.Type.DISCHARGE) {
					throw new RuntimeException("该运单已卸货");
				}
			}
			transport.setStatus(behavior.getType());
			transportService.modify(transport);
		}
		behavior.setTransport(transport);
		super.add(behavior);
		Scan scan = behavior.getScan();
		if (scan != null) {
			scan.setBehavior(behavior);
			scanService.add(scan);
		}

		return behavior;
	}

	@Override
	protected Predicate toPredicate(Behavior behavior, Root<Behavior> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.isNull(root.get("auditTime")));
		predicates.add(cb.equal(root.get("type").as(Behavior.Type.class), Behavior.Type.IN));
		if (behavior.getAuditor() != null) {
			predicates.add(cb.equal(root.get("transport").get("constructionSite").get("director").as(Director.class), behavior.getAuditor()));
		}
		return cb.and(predicates.toArray(new Predicate[predicates.size()]));
	}

	public void auditing(Behavior behavior) {
		Behavior entity = super.findOne(behavior.getId());
		entity.setAuditTime(new Date());
		entity.setComments(behavior.getComments());
		entity.setAuditor(behavior.getAuditor());
		if (entity.getAuditor() == null) {
			throw new RuntimeException("审核人不能为空.");
		}
		Transport transport = entity.getTransport();

		Director director = transport.getConstructionSite().getDirector();
		if (director == null || !director.getId().equals(behavior.getAuditor().getId())) {
			throw new RuntimeException("审核人不是该工地主管.");
		}
		super.modify(entity);

		transport.setAuditTime(new Date());
		transport.setComments(entity.getComments());
		transport.setAuditor(entity.getAuditor());
		transportService.modify(transport);

		Transport example = behavior.getTransport();
		if (example != null) {
			if (example.getPrice() != null) {
				transport.setPrice(example.getPrice());
			}
			if (example.getFully() != null) {
				transport.setFully(example.getFully());
			}
			if (example.getMateriel() != null && example.getMateriel().getId() != null) {
				transport.setMateriel(example.getMateriel());
			}
			transportService.modify(transport);
		}

	}

}
