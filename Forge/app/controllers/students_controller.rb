class StudentsController < ApplicationController

	include SessionHelper

  def list
  	return if sessionAdminCheckFailed

    @students = User.where(:permissions => 'student').order(lastName: :desc)
  end

  def show
  	return if sessionAdminCheckFailed

  	@student = User.find(params[:id])
    if @student.nil?
      flash[:error] = "Cannot find find a student with ID: #{params[:id]}"
      redirect_to students_list_path
      return
    end
  end

  def destroy
    return if sessionAdminCheckFailed

    @student = User.find(params[:id])
    if @student.nil?
      flash[:error] = "Cannot find find the student account with ID: #{params[:id]}"
    else
      flash[:notice] = "Account #{@student.username} deleted successfully."
      @student.delete
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

    if params[:arenaLocation].nil? or params[:arenaLocation].empty?
      failureString += 'Zero length Arena Location. '
      didPass = false
    end

    # Check existence
    if didPass
      user = User.create(username: params[:username],
                        firstName: params[:firstName],
                        lastName: params[:lastName],
                        studentID: params[:studentID],
                        permissions: 'student',
                        password: SecureRandom.uuid.split('-')[4],
                        guid: SecureRandom.uuid,
                        arenaLocation: params[:arenaLocation])

      flash[:notice] = "Account #{user.username} Created Successfully."
      redirect_to students_new_path
    end

    if not didPass
      flash[:error] = failureString
      redirect_to students_new_path
    end

  end
end
