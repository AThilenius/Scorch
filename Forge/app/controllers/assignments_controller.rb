class AssignmentsController < ApplicationController

  include SessionHelper

  def show
    return if sessionActiveCheckFailed


  end

  def list
    return if sessionActiveCheckFailed


  end
end
