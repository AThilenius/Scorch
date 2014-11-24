module SessionHelper

	def sessionIsLoggedIn
		return (session[:username] != nil and session[:username] != "")
	end

	def sessionIsAdmin
		return (sessionIsLoggedIn and sessionGetUser.permissions == 'admin')
	end

	def sessionGetUser
		return User.new(session[:username])
	end

	def sessionLogin(user)
		session[:username] = user.username
	end

	def sessionLogout
		session[:username] = nil
	end

	def sessionActiveCheck
		if not sessionIsLoggedIn
			flash[:error] = "Please sign into Forge first."
			redirect_to login_path
		end
	end

end