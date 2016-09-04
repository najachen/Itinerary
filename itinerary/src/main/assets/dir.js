
var myLatLng;
var map;

function showMyCurrentPosition() {

	// 先將地圖定位到我的當前位置
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(myCurrentPosition);
	} else {
		alert('瀏覽器不支援 HTML5 的導航功能!!');
	}

	// 我的位置
	function myCurrentPosition(currentPosition) {
		var coords = currentPosition.coords;
		var latitude = coords.latitude;
		var longitude = coords.longitude;

		myLatLng = new google.maps.LatLng(latitude, longitude, false);

		// MapOptions options
		var options = {
			zoom : 16,
			center : myLatLng,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		}
		map = new google.maps.Map($('#map_canvas'), options);
	}
}

function reflashDirection(site) {

	// DirectionsRendererOptions options
	var options = {
		'map' : map,
		'preserveViewport' : true, //可保有原本的視野來規畫與繪製路徑
		'draggable' : true //可藉由拖動改變路徑
	}

	var renderer = new google.maps.DirectionsRenderer(options);
	//目前暫不用儀表板
//	renderer.setPanel($('#map_panel'));
	
	//DirectionsRequest request
	var request = {
		origin:myLatLng,
		destination:site,
		travelMode:google.maps.TravelMode.WALKING
	}
	
	var directionsService = new google.maps.DirectionsService();
	directionsService.route(request, routeResult);
	
	function routeResult(result, status){
		//DirectionsStatus status
		if(status == google.maps.DirectionsStatus.OK){
			//DirectionsResult result
			renderer.setDirections(result);
		}
	}
}
