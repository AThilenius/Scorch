class AnvilController < ApplicationController
  skip_before_filter :verify_authenticity_token, :only => [:login]

  def login
  	information = request.raw_post
		data_parsed = JSON.parse(information)
		p data_parsed
  	render :json => { :returnField => "Value" }
  end
  
end