class PasswordController < ApplicationController

	include SessionHelper

  def show
  	return if sessionAdminCheckFailed
  end

  def change
  	return if sessionAdminCheckFailed

  	currentUser = sessionGetUser
  	user = User.fromUsername(params[:username]);
  	didPass = true
  	failureString = ''

		if user == nil
			failureString += 'Wrong user name or password. '
			didPass = false
		elsif params[:password] != user.password
			failureString += 'Wrong user name or password. '
			didPass = false
		end

		if params[:newPasswordOne] != params[:newPasswordTwo]
			failureString += "New passwords don't match. "
			didPass = false
		elsif params[:newPasswordOne].length < 6
			failureString += 'New password too short. '
			didPass = false
		end

		if didPass
			user.password = params[:newPasswordOne]
			flash[:notice] = 'Password successfully changed.'
			redirect_to account_path
		else
			flash[:error] = failureString
			redirect_to password_path
		end

  end
end
