package com.mysema.query.jpa.domain18;

public class Route1 {

	private RouteDbKey routeKey;
    private int version;
    private int cost;
    
	public RouteDbKey getRouteKey() {
		return routeKey;
	}
	public void setRouteKey(RouteDbKey routeKey) {
		this.routeKey = routeKey;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	
}
