class ServiceController < ApplicationController
  skip_before_filter :verify_authenticity_token, :only => [:get_minecraft_session]

  def get_minecraft_session
    information = request.raw_post
    data_parsed = JSON.parse(information)
    p data_parsed
    render :json => { :user_args => "--username deathsshado0 --uuid a84c72ce2e9445e4b220914678f2cb6d --accessToken 6fb0da0f258f4fd9b63473e5fff72119 --userProperties {} --userType legacy" }
  end
end
