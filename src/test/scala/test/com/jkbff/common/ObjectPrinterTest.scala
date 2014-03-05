package test.com.jkbff.common

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import com.jkbff.common.ObjectPrinter
import com.jkbff.common.Printer

@RunWith(classOf[JUnitRunner])
class ObjectPrinterTest extends FunSuite {
	test("getFields()") {
		val objectPrinter = new ObjectPrinter

		val obj = new TestClass2("hi", "bye1")
		
		val expected = List("nest1", "nest2", "intVal", "longInt", "dub", "floatingPoint", "shortVal", "byteVal", "charVal", "someList", "someSet", "someMap", "hello", "goodBye")
		
		assert(objectPrinter.getFields(obj).map(x => x.getName()).toList == expected)
	}
	
	test("getClassHierarchy()") {
		val objectPrinter = new ObjectPrinter

		val testclass = new TestClass2("hi", "bye1")
		
		val expected = List(classOf[TestClass2], classOf[TestClass])
		val result = objectPrinter.getClassHierarchy(testclass.getClass()).takeWhile(_.getName() != "java.lang.Object").toList
		
		assert(result == expected)
	}

	test("checkForCustomPrinter()") {
		val testPrinter = new ObjectPrinter

		class Test1 {

		}

		class Test2 extends Test1 {

		}

		class Test3 extends Test2

		testPrinter.addCustom(classOf[Test1], new Printer {
			def printObj[T](name: String, obj: T, prefix: String, showTypes: Boolean): String = {
				"\n" + prefix + testPrinter.prefixInc + "test1: " + obj.getClass
			}
		})

		val expected = "obj = \n  test1: class test.com.jkbff.common.ObjectPrinterTest$$anonfun$3$Test3$1\n"
		val result = testPrinter.printObj(new Test3, false)

		assert(result == expected)
	}
	
	test("printObj() for a string") {
		val objectPrinter = new ObjectPrinter

		val expected = "obj: String = hi\n"
		
		assert(objectPrinter.printObj("hi", true) == expected)
	}
	
	test("printObj() for an object with no fields") {
		val objectPrinter = new ObjectPrinter

		val testclass = new AnyRef
		
		val expected = "obj = \n  <no fields>\n"
		
		assert(objectPrinter.printObj(testclass, false) == expected)
	}
	
	test("printObj() for a java Collection") {
		val objectPrinter = new ObjectPrinter

		val list = new java.util.ArrayList[String]
		list.add("hello")
		list.add("hey")
		list.add("hi")
		
		val expected = "obj: java.util.ArrayList = \n  [0]: String = hello\n  [1]: String = hey\n  [2]: String = hi\n"
		
		assert(objectPrinter.printObj(list, true) == expected)
	}
	
	test("printObj() for a java map") {
		val objectPrinter = new ObjectPrinter

		val map = new java.util.HashMap[String, String]()
		map.put("first", "a1")
		map.put("second", "a2")
		map.put("third", "a3")
		
		val expected = "obj: java.util.HashMap = \n  [second]: String = a2\n  [third]: String = a3\n  [first]: String = a1\n"
		
		assert(objectPrinter.printObj(map, true) == expected)
	}
	
	test("printObj() for a complex object") {
		val objectPrinter = new ObjectPrinter

		val expected = "obj: test.com.jkbff.common.TestClass2 = \n  nest1: test.com.jkbff.common.TestClass = \n    hello: String = hola\n    goodBye: String = adios\n  nest2: test.com.jkbff.common.TestClass = \n    hello: String = bonjour!\n    goodBye: String = adieu\n  intVal: Integer = 987\n  longInt: Long = 1234\n  dub: Double = 12.34\n  floatingPoint: Float = 123.456\n  shortVal: Short = 123\n  byteVal: Byte = 111\n  charVal: Character = J\n  someList: scala.collection.immutable.$colon$colon = \n    [0]: Integer = 1\n    [1]: Integer = 2\n    [2]: Integer = 3\n  someSet: scala.collection.immutable.Set$Set3 = \n    [0]: String = a\n    [1]: String = b\n    [2]: String = c\n  someMap: scala.collection.immutable.HashMap$HashTrieMap = \n    [cc]: String = jamie\n    [aa]: String = jason\n    [bb]: String = jodie\n    [ee]: String = casey\n    [ff]: String = cody\n    [dd]: String = kelsie\n  hello: String = hi\n  goodBye: String = bye1\n"
		
		assert(objectPrinter.printObj(new TestClass2("hi", "bye1"), true) == expected)
	}
}

class TestClass(val hello: String, val goodBye: String) {
	
}

class TestClass2(hello: String, goodBye: String) extends TestClass(hello, goodBye) {
	val nest1 = new TestClass("hola", "adios")
	val nest2 = new TestClass("bonjour!", "adieu")
	val intVal = 987
	val longInt = 1234L
	val dub = 12.34
	val floatingPoint = 123.456f
	val shortVal = 123.toShort
	val byteVal = 111.toByte
	val charVal = 'J'
	val someList = Seq(1, 2, 3)
	val someSet = Set("a", "b", "c")
	val someMap = Map("aa" -> "jason", "bb" -> "jodie", "cc" -> "jamie", "dd" -> "kelsie", "ee" -> "casey", "ff" -> "cody")
}