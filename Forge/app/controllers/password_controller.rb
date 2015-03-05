class PasswordController < ApplicationController

	include SessionHelper

  def show
  	return if sessionActiveCheckFailed
  end

  def change
  	return if sessionActiveCheckFailed

  	currentUser = sessionGetUser
  	didPass = true
  	failureString = ''

		if params[:password] != currentUser.password
			failureString += 'Incorrect current password. '
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
			currentUser.password = params[:newPasswordOne]
			currentUser.save
			flash[:notice] = 'Password successfully changed.'
			redirect_to account_path
		else
			flash[:error] = failureString
			redirect_to password_path
		end

  end
end
