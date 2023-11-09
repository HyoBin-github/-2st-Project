package org.spring.dev.openApi.bus.busRoute;

import lombok.Data;

import java.util.List;

@Data
public class MsgBody{

  private List<ItemList> itemList;
}
