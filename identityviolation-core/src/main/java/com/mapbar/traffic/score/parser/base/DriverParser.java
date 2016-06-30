package com.mapbar.traffic.score.parser.base;

import com.mapbar.traffic.score.base.DriverProfile;

public interface DriverParser {

	String parse(String strMsg, DriverProfile driverProfile);

}
