#!/usr/bin/env ruby


require 'open-uri'
require 'nokogiri'
require 'sqlite3'

def getCurrencyExchange()
  url = "http://www.cbr.ru/scripts/XML_daily.asp"

  currStore = Hash.new
  doc = Nokogiri::XML(open(url))
  date = doc.root['Date']
  currStore['Date'] = date

  doc.css('Valute').each do |node|
    key = node.css('CharCode').text
    currStore[key] = node.css('Value').text
  end
  return currStore
end

db = SQLite3::Database.new 'ExchangesRate.db'

# Create a table
result = db.execute <<-SQL
    CREATE TABLE IF NOT EXISTS ExchangesRate (
      date TEXT PRIMARY KEY,
      value TEXT
    );
SQL

loop do
  store = getCurrencyExchange()
  p store['Date'] + " "+store['USD']

  db.execute('insert or ignore into ExchangesRate (date, value) values (?, ?)', [store['Date'],store['USD']])
  sleep 3600 * 12 # wait 12h
end

