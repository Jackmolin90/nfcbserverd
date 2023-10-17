package com.imooc.utils;

/**
 * @author zhc 2021-09-08 15:12
 */
public enum NfcOperatorEnum {
				 	
    Exch("Exch","0x4e46433a313a45786368"),
    Bind("Bind","0x4e46433a313a42696e64"),
    Unbind("Unbind","0x4e46433a313a556e62696e64"),
    Rebind("Rebind","0x4e46433a313a526562696e64"),
    CandReq("CandReq","0x4e46433a313a43616e64526571"),
    CandExit("CandExit","0x4e46433a313a43616e6445786974"),
    CandPnsh("CandPnsh","0x4e46433a313a43616e64506e7368"),
    FlwReq("FlwReq","0x4e46433a313a466c77526571"),
    FlwExit("FlwExit","0x4e46433a313a466c7745786974"),
	flwrpten("flwrpten","0x4e46433a313a666c77727074656e"),
	;

    private String code;
    private String encode;

    NfcOperatorEnum(String code, String encode) {
        this.code = code;
        this.encode = encode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }
}
