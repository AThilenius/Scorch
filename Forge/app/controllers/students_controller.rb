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

  def destroy
    return if sessionAdminCheckFailed

    @student = User.get(params[:username])
    if @student.nil?
      flash[:error] = "Cannot find find the student account: #{params[:username]}"
    else
      @student.delete
      flash[:notice] = "Account #{@student.key} deleted successfully."
    end

    redirect_to students_list_path
  end

  def new
    return if sessionAdminCheckFailed
  end

  def create

    return if sessionAdminCheckFailed
    didPass = true
    failureString = ''

    if params[:username].nil? or params[:username].empty?
      failureString += 'Zero length user name. '
      didPass = false
    end

    if params[:firstName].nil? or params[:firstName].empty?
      failureString += 'Zero length first name. '
      didPass = false
    end

    if params[:lastName].nil? or params[:lastName].empty?
      failureString += 'Zero length last name. '
      didPass = false
    end

    if params[:studentID].nil? or params[:studentID].empty?
      failureString += 'Zero length student ID. '
      didPass = false
    end

    # Check existence
    if didPass
      user = User.create(params[:username])
      user.firstName = params[:firstName]
      user.lastName = params[:lastName]
      user.studentID = params[:studentID]
      user.permissions = 'student'
      user.password = SecureRandom.uuid.split('-')[4]

      flash[:notice] = "Account #{user.key} Created Successfully."
      redirect_to students_list_path
    end

    if not didPass
      flash[:error] = failureString
      redirect_to students_new_path
    end

  end
end
