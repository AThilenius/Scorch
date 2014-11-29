class AssignmentDescription

  include ApplicationHelper

  #=AssignmentDescription
  # AssignmentDescription_meta:all
  # AssignmentDescription:<assignment name>:Description Markdown
  # AssignmentDescription:<assignment name>:Due Date
  # AssignmentDescription:<assignment name>:Download ZIP Path
  # AssignmentDescription:<assignment name>:Jar Path
  # AssignmentDescription:<assignment name>:Levels [SET]

  @@classTypeName = 'AssignmentDescription'

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
    redis_accessor @@classTypeName + ':' + key + ':', :descriptionMarkdown, :dueDate, :jarPath
  end

  def delete
    # simply remove it from the set
    $redis.srem(@@classTypeName + '_meta:all', @key)
  end

  def key
    return @key
  end


  # AssignmentDescription:<assignment name>:Levels [SET]
  def allLevels
    setData = []
    $redis.smembers(@@classTypeName + ':' + @key + ':levels').each do |key|
      setData << LevelDescription.new(key)
    end

    return setData
  end

  def addLevel(key)
    $redis.sadd(@@classTypeName + ':' + @key + ':levels', key)
  end

  def removeLevel(key)
    $redis.srem(@@classTypeName + ':' + @key + ':levels', @key)
  end

  
end