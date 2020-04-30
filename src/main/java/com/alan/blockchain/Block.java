package com.alan.blockchain;

import com.alan.util.EncryptionDecryptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @Description: 区块
 * @Author MengQingHao
 * @Date 2020/4/30 10:56 上午
 * @Version 1.3.0
 */
public class Block {

    private static final Logger LOGGER = LoggerFactory.getLogger(Block.class);

    private String hash;

    private String prevHash;

    private String data;

    private long timeStamp;

    private int nonce;

    public Block(String data, String prevHash) {
        this.data = data;
        this.prevHash = prevHash;
        this.timeStamp = System.currentTimeMillis();
        this.hash = calculateHash();
    }

    /**
     * 创建hash值
     * @return java.lang.String
     * @author MengQingHao
     * @date 2020/4/30 11:50 上午
     * @version 1.3.0
     */
    private String calculateHash() {
        StringBuilder input = new StringBuilder(prevHash).append(timeStamp).append(nonce).append(data);
        return EncryptionDecryptionUtil.encryptSha256(input.toString());
    }

    /**
     * 校验hash值
     * @return boolean
     * @author MengQingHao
     * @date 2020/4/30 11:54 上午
     * @version 1.3.0
     */
    public boolean checkHash() {
        return Objects.equals(calculateHash(), hash);
    }

    /**
     * 挖矿
     * @param difficulty
     * @return void
     * @author MengQingHao
     * @date 2020/4/30 12:07 下午
     * @version 1.3.0
     */
    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while(!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        LOGGER.info("Block Mined!!! : {}", hash);
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public void setPrevHash(String prevHash) {
        this.prevHash = prevHash;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

}