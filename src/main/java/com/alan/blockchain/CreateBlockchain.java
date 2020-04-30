package com.alan.blockchain;

import com.alibaba.fastjson.JSON;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description:
 * @Author MengQingHao
 * @Date 2020/4/30 12:02 下午
 * @Version 1.3.0
 */
public class CreateBlockchain {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBlockchain.class);

    public static void main(String[] args) {
        int difficulty = 6;
        List<Block> blockchain = Lists.newArrayList();
        Block first = new Block("Hi im the first block", "0");
        blockchain.add(first);
        LOGGER.info("Trying to Mine block 1... ");
        first.mineBlock(difficulty);

        Block second = new Block("Yo im the second block", first.getHash());
        blockchain.add(second);
        LOGGER.info("Trying to Mine block 2... ");
        second.mineBlock(difficulty);

        Block third = new Block("Hey im the third block", second.getHash());
        blockchain.add(third);
        LOGGER.info("Trying to Mine block 3... ");
        third.mineBlock(difficulty);

        LOGGER.info("Blockchain is Valid: {} \n {}", CheckBlock.isChainValid(blockchain, difficulty), JSON.toJSONString(blockchain));
    }




}
