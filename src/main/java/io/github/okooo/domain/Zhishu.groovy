package io.github.okooo.domain

import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.Table

@Table
class Zhishu {

    @Id
    Long id

    @Column(length = 10)
    String serial

    @Column(length = 20)
    String matchType

    @Column(length = 20)
    String matchTime

    @Column(length = 40)
    String hostTeam

    @Column(length = 40)
    String guestTeam

    @Column(length = 100)
    String remind

    @Column(length = 10)
    String matchResult

    Date createdTime
    Date updatedTime

}
