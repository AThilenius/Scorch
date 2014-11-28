class StudentsController < ApplicationController

	include SessionHelper

  def list
  	return if sessionAdminCheckFailed
  end

  def show
  	return if sessionAdminCheckFailed

  	@student = User.get(params[:username])
    if @student.nil?
      flash[:error] = "Cannot find find a student with that username: #{params[:username]}"
      redirect_to students_list_path
    end
  end
end
