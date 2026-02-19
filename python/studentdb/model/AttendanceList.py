from typing import Dict, Iterator, KeysView, ValuesView, ItemsView

from studentdb.model.CreateCourseName import CreateCourseName
from studentdb.model.CourseAttendanceData import CourseAttendanceData


class AttendanceList:
    """
    A wrapper around a dictionary that maps course names to attendance data.
    This class implements the dictionary interface by delegating to an internal dict.
    """

    def __init__(self):
        self._delegate: Dict[CreateCourseName, CourseAttendanceData] = {}

    def size(self) -> int:
        return len(self._delegate)

    def isEmpty(self) -> bool:
        return len(self._delegate) == 0

    def containsKey(self, key: object) -> bool:
        return key in self._delegate

    def containsValue(self, value: object) -> bool:
        return value in self._delegate.values()

    def get(self, key: object) -> CourseAttendanceData:
        return self._delegate.get(key)

    def put(self, key: CreateCourseName, value: CourseAttendanceData) -> CourseAttendanceData:
        old_value = self._delegate.get(key)
        self._delegate[key] = value
        return old_value

    def remove(self, key: object) -> CourseAttendanceData:
        return self._delegate.pop(key, None)

    def putAll(self, m: Dict[CreateCourseName, CourseAttendanceData]) -> None:
        self._delegate.update(m)

    def clear(self) -> None:
        self._delegate.clear()

    def keySet(self) -> KeysView[CreateCourseName]:
        return self._delegate.keys()

    def values(self) -> ValuesView[CourseAttendanceData]:
        return self._delegate.values()

    def entrySet(self) -> ItemsView[CreateCourseName, CourseAttendanceData]:
        return self._delegate.items()

    def __len__(self) -> int:
        return len(self._delegate)

    def __iter__(self) -> Iterator[CreateCourseName]:
        return iter(self._delegate)

    def __contains__(self, key: object) -> bool:
        return key in self._delegate

    def __getitem__(self, key: CreateCourseName) -> CourseAttendanceData:
        return self._delegate[key]

    def __setitem__(self, key: CreateCourseName, value: CourseAttendanceData) -> None:
        self._delegate[key] = value
