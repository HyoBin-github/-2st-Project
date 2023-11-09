package org.spring.dev.openApi.bus.busRoute;

import lombok.Data;

@Data
public class MsgHeader {

  private String headerMsg;
  private String headerCd;
  private String itemCount;
}
