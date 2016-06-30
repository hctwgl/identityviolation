package com.mapbar.traffic.score.transfer.test;

import org.junit.Test;

import com.mapbar.traffic.score.transfer.FreshYzm;
import com.mapbar.traffic.score.transfer.citys.CityPanjinService;
import com.mapbar.traffic.score.transfer.factory.TransferFactory;

public class TestTransferFactory {
	@Test
	public void test() throws Exception {
		System.out.println(TransferFactory.getInstance("com.mapbar.traffic.score.transfer.ShenyangService"));
		System.out.println(TransferFactory.getInstance("com.mapbar.traffic.score.transfer.ShenzhenService"));
		System.out.println(TransferFactory.getInstance("com.mapbar.traffic.score.transfer.ShenyangService"));
		System.out.println(TransferFactory.getInstance("com.mapbar.traffic.score.transfer.ShenzhenService"));
		System.out.println(TransferFactory.getInstance("com.mapbar.traffic.score.transfer.yzm.CityPanjinService"));
		FreshYzm fresh = (FreshYzm) TransferFactory.getInstance("com.mapbar.traffic.score.transfer.yzm.CityPanjinService");
		CityPanjinService instanceByClass = TransferFactory.getInstanceByClass(CityPanjinService.class);
		System.out.println(fresh);
		System.out.println(instanceByClass);

	}
}
