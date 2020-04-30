package com.alan.blockchain;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description: 校验
 * @Author MengQingHao
 * @Date 2020/4/30 11:42 上午
 * @Version 1.3.0
 */
public class CheckBlock {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckBlock.class);

    /**
     * 校验区块链
     * @param blockchain
     * @param difficulty
     * @return boolean
     * @author MengQingHao
     * @date 2020/4/30 11:48 上午
     * @version 1.3.0
     */
    public static boolean isChainValid(List<Block> blockchain, int difficulty) {
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for(int i=1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block prevBlock = blockchain.get(i-1);
            if(!currentBlock.checkHash()){
                LOGGER.info("Current Hashes not equal =》{}", JSON.toJSONString(currentBlock));
                return false;
            }
            if(!prevBlock.checkHash()) {
                LOGGER.info("Prev Hashes not equal =》{}", JSON.toJSONString(prevBlock));
                return false;
            }
            //check if hash is solved
            if(!currentBlock.getHash().substring( 0, difficulty).equals(hashTarget)) {
                LOGGER.info("This block hasn't been mined =》{}", JSON.toJSONString(currentBlock));
                return false;
            }
        }
        return true;
    }
}
