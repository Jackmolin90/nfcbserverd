package com.imooc.pojo.Form;

import com.imooc.form.BasePo;
import com.imooc.form.BaseQueryForm;
import com.imooc.pojo.Param.BlockQueryParam;
import lombok.Data;

@Data
public class NfcNodeMinerQueryForm extends BaseQueryForm<BlockQueryParam> {

    private String  node_address;

    private  String revenue_address;

    private Integer node_type;

    private String address;

    public NfcNodeMinerQueryForm() {
    }

    public NfcNodeMinerQueryForm(String node_address) {
        this.node_address = node_address;
    }
}
