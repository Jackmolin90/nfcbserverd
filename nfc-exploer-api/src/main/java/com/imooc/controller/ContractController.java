package com.imooc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.pojo.Contract;
import com.imooc.pojo.ContractLockup;
import com.imooc.pojo.ContractSource;
import com.imooc.pojo.ContractVersion;
import com.imooc.pojo.Form.BlockQueryForm;
import com.imooc.service.ContractService;
import com.imooc.utils.Constants;
import com.imooc.utils.ResultMap;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/platform")
@Api(tags="ContractController")
public class ContractController {
	
    @Autowired
    private ContractService contractService;    
    
    @ApiOperation(value = "getContractList")
	@PostMapping("/getContractList")
	public ResultMap<Page<Contract>> getContractList(@Valid @RequestBody BlockQueryForm blockQueryForm) {
    	blockQueryForm.setContract(Constants.pre0XtoNX(blockQueryForm.getContract()));
		return contractService.getContractList(blockQueryForm);
	}

    @ApiOperation(value = "getContractInfo")
	@RequestMapping("/getContractInfo")
	public ResultMap<Contract> getContractInfo(@RequestParam(value = "contract", required = true) String contract) {
		contract = Constants.pre0XtoNX(contract);
		return contractService.getContractInfo(contract);
	}
    
    @ApiOperation(value = "getContractVersions")
    @RequestMapping("/getContractVersions")
   	public ResultMap<List<ContractVersion>> getContractVersions() {    	
   		return contractService.getContractVersions();
   	}
    
    @ApiOperation(value = "getContractSources")
   	@PostMapping("/getContractSources")
   	public ResultMap<List<ContractSource>> getContractSources(@RequestParam(value = "contract", required = true) String contract) {
    	contract = Constants.pre0XtoNX(contract);
   		return contractService.getContractSources(contract);
   	}
    
    @ApiOperation(value = "verifyContractSources")
    @PostMapping("/verifyContractSources")
	public ResultMap<?> verifyContractSources(@RequestParam(value = "contract", required = true) String contract,
			@RequestParam(value = "name", required = true) String name, 
			@RequestParam(value = "version", required = true) String version, 
			@RequestParam(value = "files", required = true) MultipartFile[] files) {
		contract = Constants.pre0XtoNX(contract);
		return contractService.verifyContractSources(contract, name, version, files);
	}
    
	@ApiOperation(value = "getLockupContracts")
	@RequestMapping("/getLockupContracts")
	public ResultMap<?> getLockupContracts(@RequestParam(value = "address", required = true) String address) {
		address = Constants.pre0XtoNX(address);
		return contractService.getLockupContracts(address);
	}

	@ApiOperation(value = "getContractLockupList")
	@PostMapping("/getContractLockupList")
	public ResultMap<Page<ContractLockup>> getContractLockupList(@Valid @RequestBody BlockQueryForm blockQueryForm) {
		blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
		blockQueryForm.setContract(Constants.pre0XtoNX(blockQueryForm.getContract()));
		blockQueryForm.setAuthAddress(Constants.pre0XtoNX(blockQueryForm.getAuthAddress()));
		return contractService.getContractLockupList(blockQueryForm);
	}


//    @ApiOperation(value = "getAbi", notes = "getAbi")
//    @RequestMapping("/contract/getAbi")
//    public ResultMap<?> getLeatestBlockNumber(@RequestParam(value="address",required=true)String address) {
//        return contractService.getContractAbi(address);
//    }


}
