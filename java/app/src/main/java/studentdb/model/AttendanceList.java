package studentdb.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AttendanceList implements Map<CreateCourseName, CourseAttendanceData> {

    private final Map<CreateCourseName, CourseAttendanceData> delegate = new HashMap<>();

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public CourseAttendanceData get(Object key) {
        return delegate.get(key);
    }

    @Override
    public CourseAttendanceData put(CreateCourseName key, CourseAttendanceData value) {
        return delegate.put(key, value);
    }

    @Override
    public CourseAttendanceData remove(Object key) {
        return delegate.remove(key);
    }

    @Override
    public void putAll(Map<? extends CreateCourseName, ? extends CourseAttendanceData> m) {
        delegate.putAll(m);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Set<CreateCourseName> keySet() {
        return delegate.keySet();
    }

    @Override
    public Collection<CourseAttendanceData> values() {
        return delegate.values();
    }

    @Override
    public Set<Entry<CreateCourseName, CourseAttendanceData>> entrySet() {
        return delegate.entrySet();
    }
}
