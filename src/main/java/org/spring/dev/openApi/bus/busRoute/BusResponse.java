package org.spring.dev.openApi.bus.busRoute;

import lombok.Data;

@Data
public class BusResponse{

  private ComMsgHeader comMsgHeader;

  private MsgHeader msgHeader;

  private MsgBody msgBody;

}
