package org.spring.dev.openApi.bus.busRoute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ApiExplorer {

  private static final String serviceKey = "NVJloZDa5SGDsI8Fh%2FRFvsncmFGpdbR2XZwJvhXquRiyzanDEWAHfz1LWFl4rhZV%2BSAttNUPMUmnVcuuk7AfxQ%3D%3D";

  // 버스 노선 검색 -> 공공데이터 포터 제공 API 예제 활용
  public static String getBusList(String strSrch) throws IOException {

    String apiURL = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList"; // 버스 목록
    StringBuilder urlBuilder = new StringBuilder(apiURL); /* URL */
    urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey); /* Service Key */
    urlBuilder.append("&" + URLEncoder.encode("strSrch", "UTF-8") + "=" + URLEncoder.encode(strSrch, "UTF-8")); /**/
    urlBuilder.append("&resultType=json");

    URL url = new URL(urlBuilder.toString());
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Content-type", "application/json");
    System.out.println("Response code: " + conn.getResponseCode());

    BufferedReader rd;
    if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
      rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    } else {
      rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
    }
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = rd.readLine()) != null) {
      sb.append(line);
    }
    rd.close();
    conn.disconnect();
    System.out.println(sb.toString());

    return sb.toString();
  }

  // 버스 노선에 해당 하는 정류장 -> 공공데이터 포터 제공 API 예제 활용

  public static String getRespose(String busRouteId) throws IOException {

    String apiURL = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute"; // 버스노선에 해당하는 정류장
    StringBuilder urlBuilder = new StringBuilder(apiURL); /* URL */
    urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey); /* Service Key */
    urlBuilder
        .append("&" + URLEncoder.encode("busRouteId", "UTF-8") + "=" + URLEncoder.encode(busRouteId, "UTF-8")); /**/
    urlBuilder.append("&resultType=json");

    URL url = new URL(urlBuilder.toString());
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Content-type", "application/json");
    System.out.println("Response code: " + conn.getResponseCode());
    BufferedReader rd;
    if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
      rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    } else {
      rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
    }
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = rd.readLine()) != null) {
      sb.append(line);
    }
    rd.close();
    conn.disconnect();
    System.out.println(sb.toString());
    return sb.toString();

  }

}