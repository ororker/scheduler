package org.jpm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GatewayPool {

	private List<GatewayConfig> externalResourceConfigs = new ArrayList<GatewayConfig>();
	
	Set<Gateway> gatewayPool = new HashSet<Gateway>();
	Iterator<Gateway> gatewayPoolIt = gatewayPool.iterator();

}
