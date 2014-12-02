class SessionController < ApplicationController

	include SessionHelper

	def login
		user = User.find_by username: params[:username];

		if user == nil
			flash[:error] = 'Wrong user name or password!'
			redirect_to login_path
		else
			if params[:password] != user.password
				flash[:error] = 'Wrong user name or password!'
				redirect_to login_path
			else
				#Login successful
				sessionLogin(user)
	    	redirect_to root_path
	  	end
		end
	end

	def logout
		sessionLogout
		redirect_to root_path
	end


end
