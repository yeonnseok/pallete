package kr.co.pallete.api.interfaces.mappers

import kr.co.pallete.api.v1.member.member
import kr.co.pallete.database.document.Member

object MemberProtoMapper {

    fun Member.toProto() = member {
        id = this@toProto.id.toString()
        name = this@toProto.name
        email = this@toProto.email
        age = this@toProto.age
    }
}
