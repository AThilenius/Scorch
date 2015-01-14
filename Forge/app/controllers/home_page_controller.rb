class HomePageController < ApplicationController

  include SessionHelper

  def index
    return if sessionActiveCheckFailed
  end
end
