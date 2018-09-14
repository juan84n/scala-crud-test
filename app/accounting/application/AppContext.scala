package accounting.application

import accounting.domain.repository.AccountRepository
import accounting.domain.repository.impl.inmemory.AccountRepositoryInMemory
import accounting.domain.repository.impl.mysql.AccountRepositoryMysql
import javax.inject.{ Inject, Singleton }

@Singleton
class AppContext @Inject() ( accountRepositoryMysql: AccountRepositoryMysql ) {
  val accountRepoInMemory: AccountRepository = AccountRepositoryInMemory
  val accountRepoMySql: AccountRepository = accountRepositoryMysql

}
