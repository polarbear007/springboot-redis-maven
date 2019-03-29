package cn.itcast;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootRedisMavenApplicationTests {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private StringRedisTemplate stringTemplate;
	
	@Test
	public void contextLoads() {
		System.out.println(stringTemplate);
		System.out.println(redisTemplate);
	}
	
	@Test
	public void test() {
		Set<String> keys = stringTemplate.keys("*");
		if(keys != null && keys.size() > 0) {
			for (String key : keys) {
				System.out.println(key);
			}
		}
	}
	
	@Test
	public void test2() {
		Set<String> keys = redisTemplate.keys("*");
		if(keys != null && keys.size() > 0) {
			for (String key : keys) {
				System.out.println(key);
			}
		}
	}
	
	// 使用 redisTemplate 保存一个对象，然后再取出来 
	@Test
	public void test3() {
		List<String> list = Arrays.asList("001", "002" , "003");
		redisTemplate.opsForValue().set("list", list);
		
		List<String> list2 = (List<String>)redisTemplate.opsForValue().get("list");
		System.out.println(list2);
	}
	
	// 使用 stringRedisTemplate 保存一个 set 类型的数据
	@Test
	public void test4() {
		List<Object> results = stringTemplate.executePipelined(new SessionCallback<List<Object>>() {

			@Override
			public List<Object> execute(RedisOperations operations) throws DataAccessException {
				for (int i = 0; i < 100; i++) {
					operations.opsForSet().add("setKey:boot", String.valueOf(i));
				}
				return null;
			}
		});
		
		System.out.println(results.size());
		// 再把这个数据取回来
		// 注意： set 集合元素不允许重复，所以我们就算存多次，里面的元素仍然是那100 个
		Set<String> members = stringTemplate.opsForSet().members("setKey:boot");
		if(members != null && members.size() > 0) {
			for (String member : members) {
				System.out.println(member);
			}
		}
	}

	
}
