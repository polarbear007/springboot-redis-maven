package cn.itcast;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.test.context.junit4.SpringRunner;

import cn.itcast.entity.Student;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentTemplateTest {
	@Autowired
	private RedisTemplate<String, Student> studentTemplate;
	
	@Autowired
	private Jackson2HashMapper studentHashMapper;
	
	// 先保存
	@Test
	public void test() {
		Student stu = new Student(1, "eric", 12);
		studentTemplate.opsForValue().set("stu:1", stu);
	}
	
	// 再获取
	@Test
	public void test2() {
		Student stu = studentTemplate.opsForValue().get("stu:1");
		System.out.println(stu);
	}
	
	// redis 里面的值是 List 
	@Test
	public void test3() {
		Student stu1 = new Student(1, "eric", 12);
		Student stu2 = new Student(2, "rose", 12);
		Student stu3 = new Student(3, "jack", 12);
		Student stu4 = new Student(4, "tom", 12);
		// 保存多个学生对象到 List 队列， V 表示 Student 对象
		studentTemplate.opsForList().leftPushAll("stus", stu1, stu2, stu3, stu4);
		
		// 获取学生对象集合
		List<Student> list = studentTemplate.opsForList().range("stus", 0, -1);
		for (Student student : list) {
			System.out.println(student);
		}
	}
	
	// redis 里面的值是 Set 
	@Test
	public void test4() {
		Student stu1 = new Student(1, "eric", 12);
		Student stu2 = new Student(2, "rose", 12);
		Student stu3 = new Student(3, "jack", 12);
		Student stu4 = new Student(4, "tom", 12);
		// 保存多个学生对象到 set 队列， V 表示 Student 对象
		studentTemplate.opsForSet().add("stuSet", stu1, stu2, stu3, stu4);
		
		// 获取学生对象集合
		Set<Student> stuSet = studentTemplate.opsForSet().members("stuSet");
		for (Student student : stuSet) {
			System.out.println(student);
		}
	}
	
	// redis 里面 值是 Sorted Set
	@Test
	public void test5() {
		Student stu1 = new Student(1, "eric", 12);
		Student stu2 = new Student(2, "rose", 13);
		Student stu3 = new Student(3, "jack", 10);
		Student stu4 = new Student(4, "tom", 11);
		studentTemplate.opsForZSet().add("stusSortedSet", stu1, stu1.getAge().doubleValue());
		studentTemplate.opsForZSet().add("stusSortedSet", stu2, stu2.getAge().doubleValue());
		studentTemplate.opsForZSet().add("stusSortedSet", stu3, stu3.getAge().doubleValue());
		studentTemplate.opsForZSet().add("stusSortedSet", stu4, stu4.getAge().doubleValue());
		
		// 获取学生对象集合
		Set<Student> stuSet = studentTemplate.opsForZSet().range("stusSortedSet", 0L, -1L);
		for (Student student : stuSet) {
			System.out.println(student);
		}
	}
	
	// redis 里面的值是 Hash 类型
	@Test
	public void test6() {
		Student stu = new Student(1, "eric", 12);
		// 保存数据
		// 这里我们需要把 student 对象转成一个 Map 集合，里面保存着各种键值对（可以使用HashMapper.toHash(obj)来转换）
		studentTemplate.opsForHash().putAll("hash_stu:" + stu.getId(), studentHashMapper.toHash(stu));
		// 获取数据
		HashOperations<String, String, Object> opsForHash = studentTemplate.opsForHash();
		// 直接获取全部的键值对数据，得到一个map 集合
		Map<String, Object> entries = opsForHash.entries("hash_stu:1");
		// 调用HashMapper.fromHash(map) 把map 集合转成一个 java 对象，
		// 但是这里有一个比较不好的地方： 必须自己强转成 Student类型 
		Student student = (Student)studentHashMapper.fromHash(entries);
		System.out.println(student);
	}
}
