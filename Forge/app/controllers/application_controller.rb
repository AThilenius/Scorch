class ApplicationController < ActionController::Base

	include SessionHelper

  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  protect_from_forgery with: :exception
end

class Fixnum
  def to_percent_s(denominator)
    return '100%' if self > denominator
    return denominator <= 0 ?
        '0%' : "#{(Float(self) / Float(denominator) * 100).round}%"
  end
end