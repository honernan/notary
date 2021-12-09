package com.victor.notary.entity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.4.0.
 */

public class Crosschain_trade extends Contract {
    private static final String BINARY = "0x608060405234801561001057600080fd5b50610b25806100206000396000f3fe608060405260043610610072576000357c0100000000000000000000000000000000000000000000000000000000900480631e279a3714610077578063420d27f9146100dc578063434d6c531461018d578063df4cd4a5146101f3578063e265e0731461024a578063e5959cdc146102af575b600080fd5b34801561008357600080fd5b506100c66004803603602081101561009a57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061037c565b6040518082815260200191505060405180910390f35b3480156100e857600080fd5b5061014b600480360360408110156100ff57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506103a2565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6101d9600480360360408110156101a357600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610488565b604051808215151515815260200191505060405180910390f35b3480156101ff57600080fd5b50610208610688565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561025657600080fd5b506102996004803603602081101561026d57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610868565b6040518082815260200191505060405180910390f35b3480156102bb57600080fd5b5061031e600480360360408110156102d257600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610913565b604051808681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018481526020018381526020018281526020019550505050505060405180910390f35b6000808273ffffffffffffffffffffffffffffffffffffffff1631905080915050919050565b600081600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060018060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060020160006101000a81548160ff02191690831515021790555082905092915050565b6000813373ffffffffffffffffffffffffffffffffffffffff16311015610574577fc25bc99519819e311493ee2241d697ef31fa7104473eddc252f13b413a47cf4033604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001828103825260248152602001807f496e61646571756174652062616c616e6365206661696c656420746f2073656e81526020017f64efbc81000000000000000000000000000000000000000000000000000000008152506040019250505060405180910390a160009050610682565b7fc25bc99519819e311493ee2241d697ef31fa7104473eddc252f13b413a47cf4033604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001828103825260218152602001807f53756666696369656e742062616c616e63652063616e2062652073656e74efbc81526020017f81000000000000000000000000000000000000000000000000000000000000008152506040019250505060405180910390a18273ffffffffffffffffffffffffffffffffffffffff166108fc839081150290604051600060405180830381858888f1935050505015801561067c573d6000803e3d6000fd5b50600190505b92915050565b60003273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156106c457600080fd5b43600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000018190555032600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555042600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600201819055504340600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600301819055503a600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206004018190555033905090565b6000808290508073ffffffffffffffffffffffffffffffffffffffff1663e50c77266040518163ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040160206040518083038186803b1580156108d057600080fd5b505afa1580156108e4573d6000803e3d6000fd5b505050506040513d60208110156108fa57600080fd5b8101908080519060200190929190505050915050919050565b60008060008060008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168673ffffffffffffffffffffffffffffffffffffffff1614151561097657600080fd5b600260008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000154600260008973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600260008a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060020154600260008b73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060030154600260008c73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206004015494509450945094509450929550929590935056fea165627a7a7230582030f3397b1c3a14245821ffbe80996d0bade5a03cf5c31a628eeea11e7f99275f0029\n";

    public static final String FUNC_GET_BALANCE = "get_balance";

    public static final String FUNC_GETPUBLICACCOUNT = "GetPublicaccount";

    public static final String FUNC_SENDCOIN = "sendcoin";

    public static final String FUNC_WRITETRANSFERINFO = "WriteTransferinfo";

    public static final String FUNC_GETBLOCKNUMBERS = "GetBlocknumbers";

    public static final String FUNC_GETTRANSFERINFO = "GetTransferinfo";

    public static final Event BALANCEJUDGEMENT_EVENT = new Event("Balancejudgement", 
            Arrays.<TypeReference<?>>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event SHOWBALANCE_EVENT = new Event("ShowBalance", 
            Arrays.<TypeReference<?>>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
    ;

    public Crosschain_trade(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Crosschain_trade(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<BigInteger> get_balance(String public_notary) {
        final Function function = new Function(FUNC_GET_BALANCE, 
                Arrays.<Type>asList(new Address(public_notary)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> GetPublicaccount(String public_notary, String hyperaccount) {
        final Function function = new Function(
                FUNC_GETPUBLICACCOUNT, 
                Arrays.<Type>asList(new Address(public_notary),
                new Address(hyperaccount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> sendcoin(String public_notary, BigInteger amount, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_SENDCOIN, 
                Arrays.<Type>asList(new Address(public_notary),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> WriteTransferinfo() {
        final Function function = new Function(
                FUNC_WRITETRANSFERINFO, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> GetBlocknumbers(String addr) {
        final Function function = new Function(FUNC_GETBLOCKNUMBERS, 
                Arrays.<Type>asList(new Address(addr)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple5<BigInteger, String, BigInteger, byte[], BigInteger>> GetTransferinfo(String originaccount, String searcher) {
        final Function function = new Function(FUNC_GETTRANSFERINFO, 
                Arrays.<Type>asList(new Address(originaccount),
                new Address(searcher)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple5<BigInteger, String, BigInteger, byte[], BigInteger>>(
                new Callable<Tuple5<BigInteger, String, BigInteger, byte[], BigInteger>>() {
                    @Override
                    public Tuple5<BigInteger, String, BigInteger, byte[], BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<BigInteger, String, BigInteger, byte[], BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (byte[]) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public List<BalancejudgementEventResponse> getBalancejudgementEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(BALANCEJUDGEMENT_EVENT, transactionReceipt);
        ArrayList<BalancejudgementEventResponse> responses = new ArrayList<BalancejudgementEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            BalancejudgementEventResponse typedResponse = new BalancejudgementEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.source = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.message = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<BalancejudgementEventResponse> balancejudgementEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, BalancejudgementEventResponse>() {
            @Override
            public BalancejudgementEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(BALANCEJUDGEMENT_EVENT, log);
                BalancejudgementEventResponse typedResponse = new BalancejudgementEventResponse();
                typedResponse.log = log;
                typedResponse.source = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.message = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<BalancejudgementEventResponse> balancejudgementEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BALANCEJUDGEMENT_EVENT));
        return balancejudgementEventObservable(filter);
    }

    public List<ShowBalanceEventResponse> getShowBalanceEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(SHOWBALANCE_EVENT, transactionReceipt);
        ArrayList<ShowBalanceEventResponse> responses = new ArrayList<ShowBalanceEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ShowBalanceEventResponse typedResponse = new ShowBalanceEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.targetaccount = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.b_message = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.balance = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ShowBalanceEventResponse> showBalanceEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, ShowBalanceEventResponse>() {
            @Override
            public ShowBalanceEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(SHOWBALANCE_EVENT, log);
                ShowBalanceEventResponse typedResponse = new ShowBalanceEventResponse();
                typedResponse.log = log;
                typedResponse.targetaccount = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.b_message = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.balance = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<ShowBalanceEventResponse> showBalanceEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SHOWBALANCE_EVENT));
        return showBalanceEventObservable(filter);
    }

    public static RemoteCall<Crosschain_trade> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Crosschain_trade.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Crosschain_trade> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Crosschain_trade.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Crosschain_trade load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Crosschain_trade(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Crosschain_trade load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Crosschain_trade(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class BalancejudgementEventResponse {
        public Log log;

        public String source;

        public String message;
    }

    public static class ShowBalanceEventResponse {
        public Log log;

        public String targetaccount;

        public String b_message;

        public BigInteger balance;
    }
}
