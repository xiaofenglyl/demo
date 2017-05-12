package com.example.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by asus-Iabx on 2017/5/5.
 */
@Service
public class JedisAdapter implements InitializingBean {




    public static void print(int index,Object obj)
    {
        System.out.println(String.format("%d,%s",index,obj));
    }

    public static void main(String[] args)
    {
        Jedis jedis = new Jedis();
        jedis.flushAll();
        jedis.set("world","hello");
        print(1,jedis.get("world"));
        jedis.rename("world","new world");
        print(1,jedis.get("new world"));
        jedis.setex("hello2",15,"exciting");
        print(2,jedis.get("hello2"));
        jedis.set("pv","100");
        jedis.incrBy("pv",7);
        print(3,jedis.get("pv"));
        String listName = "ListA";

        for(int i = 0;i < 10;++i)
        {
            jedis.lpush(listName,"a"+String.valueOf(i));
        }
        print(4,jedis.lrange(listName,0,12));
        print(5,jedis.lpop(listName));
        JedisPool pool =new JedisPool();
       for(int j = 0;j < 100; ++j)
       {
           Jedis je = pool.getResource();
           je.get("a");
           System.out.println("POOL" + j);
           je.close();
       }
    }

    private Jedis jedis = null;
    private JedisPool pool = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost",6379);
    }

    private Jedis getJedis()
    {
        return pool.getResource();
    }

    public long sadd(String key,String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            return 0;
        } finally {
            {
                if (jedis != null)
                    jedis.close();
            }
        }
    }

    public boolean sismember(String key,String value)
    {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        }catch(Exception e)
        {
            return false;
        }
        finally {
            {
                if(jedis != null)
                    jedis.close();
            }
        }
    }

    public long scard(String key)
    {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch(Exception e)
        {
            return 0;
        }
        finally {
            {
                if(jedis != null)
                    jedis.close();
            }
        }
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }






}
