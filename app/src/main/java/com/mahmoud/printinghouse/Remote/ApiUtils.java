package com.mahmoud.printinghouse.Remote;

public class ApiUtils {

    public static PrintingService getUserService() {
        return RetrofitClient.getClient().create(PrintingService.class);
    }


}