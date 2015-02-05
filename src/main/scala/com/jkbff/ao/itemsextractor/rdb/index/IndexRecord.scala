package com.jkbff.ao.itemsextractor.rdb.index

class IndexRecord(val resourceType: Long, val resourceId: Long, val offset: Long) {
	override def toString() = (resourceType, resourceId, offset).toString
}