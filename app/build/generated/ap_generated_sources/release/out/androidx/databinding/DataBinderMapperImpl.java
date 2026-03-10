package androidx.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new de.sysitprep.app.DataBinderMapperImpl());
  }
}
