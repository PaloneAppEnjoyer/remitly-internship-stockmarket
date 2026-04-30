package di

import org.koin.dsl.module
import repository.ExposedStockMarketRepository
import repository.StockMarketRepository
import service.StockWalletService
import service.StockWalletServiceImpl

val appModule = module {
    single<StockMarketRepository>{ ExposedStockMarketRepository() }
    single<StockWalletService>{ StockWalletServiceImpl(get()) }
}