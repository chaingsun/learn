package com.truck.monitor.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;

import com.truck.monitor.domain.Behavior;
import com.truck.monitor.domain.Driver;
import com.truck.monitor.domain.Transport;

public interface TransportRepository extends HDRepository<Transport> {

	Transport findByDriverAndStatusNot(Driver driver, Behavior.Type status);

	/**
	 * 
	 * @param materiel
	 * @param years
	 * @param months
	 * @return
	 */
	@Query("select new Map(t.constructionSite as site,t.materiel as materiel ,t.year as year,t.month as month,count(1) as tranTimes,sum(case when t.fully > 0 then 1 else 0 end ) as fullyCount,sum(t.price) as totalPrice) from Transport t  where t.status = 'DISCHARGE' and t.year in (?1) and t.month in (?2) group by t.year,t.month,t.materiel order by t.materiel.id,t.constructionSite.id,t.year,month desc")
	List<Map<String, Object>> findStatisticsByYearsAndMonths(Integer[] years, Integer[] months);

	/**
	 * 
	 * @param materiel
	 * @param years
	 * @param months
	 * @return
	 */
	@Query("select new Map(t.constructionSite as site,t.materiel as materiel ,t.year as year,t.month as month,count(1) as tranTimes,sum(case when t.fully > 0 then 1 else 0 end ) as fullyCount,sum(t.price) as totalPrice) from Transport t  where t.status = 'DISCHARGE' and t.driver = ?1 and t.year in (?2) and t.month in (?3) group by t.year,t.month,t.materiel order by t.materiel.id,t.constructionSite.id,t.year,month desc")
	List<Map<String, Object>> findStatisticsByYearsAndMonths4Driver(Driver driver, Integer[] years, Integer[] months);
}
