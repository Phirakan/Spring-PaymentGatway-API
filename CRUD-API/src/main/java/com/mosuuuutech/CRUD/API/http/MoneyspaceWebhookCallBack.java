package com.mosuuuutech.CRUD.API.http;

import com.mosuuuutech.CRUD.API.beans.MoneyspaceWebhookDto;

public interface MoneyspaceWebhookCallBack {


    void handle (MoneyspaceWebhookDto moneyspaceWebhookDto) throws Exception;
}
