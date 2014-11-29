class User

	include ApplicationHelper

	#=User
	# user_meta:all
	# user:<CU ID>:firstName
	# user:<CU ID>:lastName
	# user:<CU ID>:password
	# user:<CU ID>:studentID
	# user:<CU ID>:permissions

	@@classTypeName = 'User'

	def self.all
		setData = []
		$redis.smembers(@@classTypeName + '_meta:all').each do |key|
			setData << new(key)
		end

		return setData
	end

	def self.get(key)
		thisKey = @@classTypeName + '_meta:all'
		if not $redis.sismember(@@classTypeName + '_meta:all', key)
			return nil
		end

		return new(key)
	end

	def self.create(key)
		if $redis.sismember(@@classTypeName + '_meta:all', key)
			return nil
		end

		# Add it to the all list
		$redis.sadd(@@classTypeName + '_meta:all', key)
		return new(key)
	end

	def initialize(key)
		@key = key
		redis_accessor @@classTypeName + ':' + key + ':', :firstName, :lastName, :password, :studentID, :permissions
	end

	def delete
		# simply remove it from the set
		$redis.srem(@@classTypeName + '_meta:all', @key)
	end

	def key
		return @key
	end

	#=Assignment Handling
	# UserAssignment:<CU ID><assignment name>:authToken
	def getUserAssignment(assignmentKey)
		userAssignment = UserAssignment.get("#{@key}#{assignmentKey}")

		if userAssignment.nil?
			# Create one if it doesn't exist.
			userAssignment = UserAssignment.create("#{@key}#{assignmentKey}")
			userAssignment.authToken = SecureRandom.uuid
		end

		return userAssignment
	end

end
