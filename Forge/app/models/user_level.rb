class UserLevel
  include ApplicationHelper

  #=UserLevel
  # UserLevel:<CU ID>_<assignment name>_<level name>:isCompleted

  @@classTypeName = 'UserLevel'

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
    redis_accessor @@classTypeName + ':' + key + ':', :isCompleted
  end

  def delete
    # simply remove it from the set
    $redis.srem(@@classTypeName + '_meta:all', @key)
  end

  def key
    return @key
  end
end