/*
 * Copyright 2015-2020 reserved by jf61.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.truck.monitor.vo;

import java.util.Date;
import java.util.List;

/**
 * 运输记录统计数据
 * Created by jufeng on 2017/7/5.
 */
public class TransportStatis {
	private Date	                    start;	      // 起始日期
	private Date	                    end;	      // 截止日期
	private String	                    licensePlate; // 车牌号
	private int	                        count;	      // 总班次数
	private Long	                    amount;	  // 总报酬
	private List<TransportStatisDetail>	details;	  // 各天内的班次数据

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public List<TransportStatisDetail> getDetails() {
		return details;
	}

	public void setDetails(List<TransportStatisDetail> details) {
		this.details = details;
	}
}
