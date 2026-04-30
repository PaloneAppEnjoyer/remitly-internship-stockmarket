package domain.exception


sealed class StockMarketException(message: String) : RuntimeException(message)

//404
class StockNotFoundException(stockName: String) :
    StockMarketException("No stock with name '$stockName' found.")

//400
class BankOutOfStockException(stockName: String) :
    StockMarketException("Stock '$stockName' is out of stock in bank.")

//400
class WalletMissingStockException(walletId: String, stockName: String) :
    StockMarketException("Wallet '$walletId' does not have stock '$stockName' to sell.")

//400
class InvalidTradeTypeException(type: String) :
    StockMarketException("Invalid trade type: '$type'. Expected 'buy' or 'sell'.")