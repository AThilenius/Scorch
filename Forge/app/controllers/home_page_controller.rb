class HomePageController < ApplicationController

  include SessionHelper

  def landing

  end

  def landing_body
    render partial: 'landing_body', :layout => false
  end

end
