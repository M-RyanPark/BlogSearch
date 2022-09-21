package com.ryan.blogsearch.infrastructure.common

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 *
 * @author M.Ryan (sh.ryan.park@dreamus.io)
 * @since 2022-09-21
 */

@ExtendWith(SpringExtension::class)
@DataJpaTest
abstract class JpaTest
